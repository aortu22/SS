import matplotlib.pyplot as plt
# import numpy as np
import csv
import json
import math

from particle import Particle


def get_particles_data(static_file_name,dynamic_file_name):
    with open(static_file_name, 'r') as config_file:
        data = json.load(config_file)
        config_file.close()
        particles = data["particles"]
        particleArray = []
        L = data["L"]
        N = data["N"]
        Rc = data["Rc"]
        maxR = 0
        for particle in particles:
            particle_id = particle["id"]
            x = particle["x"]
            y = particle["y"]
            r = particle["r"]
            if maxR >= r:
                maxR = r
            particleArray.append(Particle(particle_id, r))
        M = int(math.floor(L / (Rc + 2 * maxR)))
    with open(dynamic_file_name, 'r') as config_file:
        data = json.load(config_file)
        config_file.close()
        particles = data["particles"]
        maxR = 0
        for particle in particles:
            particle_id = particle["id"]
            x = particle["x"]
            y = particle["y"]
            getParticle(particle_id,particleArray).set_postion(x,y)

    return particleArray, L, N, M

def getParticle(particle, particles):
    for p in particles:
        if p.id == particle:
            return p

def main():
    id_particle = 'p2'
    particles, L, N, M = get_particles_data("../java/main/static.json","../java/main/dynamic.json")
    print(particles)
    print(L)
    print(N)
    print(M)
    time = 0
    with open('output.txt') as particles_data:
        next(particles_data)  # Skip first line
        particle_reader = csv.reader(particles_data, delimiter='\t')
        for particle in particle_reader:
            particle.remove('-')
            if particle[0] == "Execution time in milliseconds":
                time = particle[1]
            else:
                particle_neighbours = particle[1].split(', ')
                for particle_object in particles:
                    if particle_object.id == particle[0]:
                        particle_object.add_neighbours(particle_neighbours[:-1])

    # Create a figure and axis
    fig, ax = plt.subplots()

    # Draw grid
    for i in range(M + 1):
        ax.axhline(i * L, color='gray', linewidth=0.5)
        ax.axvline(i * L, color='gray', linewidth=0.5)
    index = 0
    for particle in particles:
        if particle.id == id_particle:
            print("found")
            break
        else:
            index += 1
    # Plot particles as dots
    for i in range(len(particles)):
        if (index==i):
            circle = plt.Circle((particles[i].x, particles[i].y), particles[i].r, color='black', fill=True) 
        else:
            circle = plt.Circle((particles[i].x, particles[i].y), particles[i].r, color='blue', fill=True) 
        ax.add_patch(circle)

    # Highlight neighbors
    for particle in particles[index].neighbours:
        p = getParticle(particle, particles)
        print(p.r)
        circle =plt.Circle((p.x, p.y), p.r, color='red', fill=True)
        ax.add_patch(circle)
    # Customize the plot
    plt.xlim(0, M * L)
    plt.ylim(0, M * L)
    plt.title("Particles and Neighbors")
    plt.xlabel("X")
    plt.ylabel("Y")
    plt.gca().set_aspect('equal', adjustable='box')

    # Show the plot
    plt.show()

if __name__ == "__main__":
    main()
