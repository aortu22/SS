import matplotlib.pyplot as plt
# import numpy as np
import csv
import json
import math

from particle import Particle


def get_particles_data(file_name):
    with open(file_name, 'r') as config_file:
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
            if maxR <= r:
                maxR = r
            particleArray.append(Particle(particle_id, x, y, r))
        M = int(math.floor(L / (Rc + 2 * maxR)))
        return particleArray, L, N, M

def getParticle(particle, particles):
    for p in particles:
        if p.id == particle:
            return p

def main():
    id_particle = 'p1'
    particles, L, N, M = get_particles_data("static.json")
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

    # Plot particles as dots
    for particle in particles:
        circle = plt.Circle((particle.x, particle.y), particle.r, color='blue', fill=False)
        ax.add_patch(circle)

    # Highlight neighbors
    index = 0
    for particle in particles:
        if particle.id == id_particle:
            break
        else:
            index += 1
    for particle in particles[index].neighbours:
        p = getParticle(particle, particles)
        plt.Circle((p.x, p.y), p.r, color='red', fill=True)


if __name__ == "__main__":
    main()
