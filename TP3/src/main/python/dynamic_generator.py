import random
import math
from particle import Particle

def checkNewParticle(particle_created,x,y,R):
    for particle in particle_created:
        if math.sqrt((x-particle.getX())**2 + (y - particle.getY())**2) <= (R * 2):
            return False
    return True


def generar_archivo(nombre_archivo, n, L,R):
    with open(nombre_archivo, 'w') as f:
        f.write("0\n")
        particle_created = []
        for _ in range(n):
            while True:
                valor1 = round(random.uniform(0, L), 2)
                valor2 = round(random.uniform(0, L), 2)
                valor3 = round(random.uniform(0, 360), 2)
                if checkNewParticle(particle_created, valor1, valor2, R):
                    break
            particle = Particle(1)
            particle.set_postion(valor1, valor2)
            particle_created.append(particle)
            print(f"{valor1} {valor2} {valor3}\n")
            f.write(f"{valor1} {valor2} {valor3}\n")

def main():
    L = 0.0
    N = 0
    with open("../java/main/static.txt", 'r') as config_file:
        L = float(next(config_file))
        L_fixed = float(next(config_file))
        M = float(next(config_file))
        N = int(next(config_file))
        R = float(next(config_file))
    generar_archivo("../java/main/dynamic.txt", N, L_fixed, R)

    destination_file_path = "../java/main/dynamicOutput.txt"  # Replace with the desired path for the destination file
    source_file_path = "../java/main/dynamic.txt"  # Replace with the actual path to the source file

    with open(source_file_path, "r") as source_file:
        content = source_file.read()
    with open(destination_file_path, "w") as destination_file:
        destination_file.write(content)


if __name__ == "__main__":
    main()