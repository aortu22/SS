import matplotlib.pyplot as plt
# import numpy as np
import csv
import math

from particle import Particle


def get_particles_data(static_file_name, dynamic_file_name):
    particleArray = []
    L = 0.0
    N = 0
    Rc = 0.0
    maxR = 0
    with open(static_file_name, 'r') as config_file:
        L = float(next(config_file))
        N = int(next(config_file))
        Rc = float(next(config_file))

        i = 0
        for linea in config_file:
            particle_id = i
            r = float(linea.split()[0])
            if maxR >= r:
                maxR = r
            particleArray.append(Particle(particle_id, r))
            i += 1
    M = int(math.floor(L / (Rc + 2 * maxR)))
    with open(dynamic_file_name, 'r') as config_file:
        # Ignorar la primera línea
        next(config_file)
        i = 0
        for linea in config_file:
            valores = linea.split()
            particle_id = i
            x = float(valores[0])
            y = float(valores[1])
            getParticle(particle_id, particleArray).set_postion(x, y)
            i += 1

    return particleArray, L, N, M


def getParticle(particle, particles):
    for p in particles:
        if p.id == particle:
            return p


def belongsTo(particle, id):
    for p in particle.neighbours:
        if p.id == id:
            return True
    return False


def main():
    id_particle = 1
    particles, L, N, M = get_particles_data("../java/main/static.txt", "../java/main/dynamic.txt")
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
                    if particle_object.id == int(particle[0]):
                        neighbours = []
                        for n in particle_neighbours[:-1]:
                            neighbours.append(particles[int(n)])
                        particle_object.add_neighbours(neighbours)


    # Create a figure and axis
    fig, ax = plt.subplots()

    # Draw grid
    for i in range(M + 1):
        ax.axhline(i * L /M, color='gray', linewidth=0.5)
        ax.axvline(i * L/M, color='gray', linewidth=0.5)

    # Plot particles as dots
    for i in range(len(particles)):
        if id_particle == i:
            circle = plt.Circle((particles[i].x, particles[i].y), particles[i].r, color='black', fill=True)
        elif belongsTo(particles[id_particle], i):
            circle = plt.Circle((particles[i].x, particles[i].y), particles[i].r, color='green', fill=True)
        else:
            circle = plt.Circle((particles[i].x, particles[i].y), particles[i].r, color='red', fill=True)
        ax.add_patch(circle)

    # Customize the plot
    plt.xlim(0, L)
    plt.ylim(0, L)
    plt.title("Particles and Neighbors")
    plt.xlabel("X")
    plt.ylabel("Y")
    plt.gca().set_aspect('equal', adjustable='box')

    # Show the plot
    plt.show()


if __name__ == "__main__":
    main()
