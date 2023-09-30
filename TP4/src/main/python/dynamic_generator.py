import random
import math
from particle import Particle

def checkNewParticle(particle_created,x,y,R):
    for particle in particle_created:
        if math.sqrt((x-particle.getX())**2 + (y - particle.getY())**2) <= (R * 2):
            return False
    return True


def generar_archivos(nombre_archivo,nombreDynamicOutput, n, L, R, V):
    print(n)
    with open(nombre_archivo, 'w') as f:
        with open(nombreDynamicOutput, 'w') as fOut:
            f.write("0\n")
            fOut.write("0\n")
            particle_created = []
            for _ in range(n):
                valor1 = round(random.uniform(0 + 3*R, L - 3*R), 6)
                valor2 = round(random.uniform(0 + 3*R, L - 3*R), 6)
                valor3 = round(random.uniform(0, 360), 4)
                while not checkNewParticle(particle_created, valor1, valor2, R):
                    valor1 = round(random.uniform(0 + 3*R, L - 3*R), 6)
                    valor2 = round(random.uniform(0 + 3*R, L - 3*R), 6)
                    valor3 = round(random.uniform(0, 360), 4)
                particle = Particle(_)
                particle.set_postion(valor1, valor2)
                particle_created.append(particle)
                f.write(f"{valor1} {valor2} {valor3}\n")
                fOut.write(f"{valor1} {valor2} {V*math.cos(math.radians(valor3))} {V*math.sin(math.radians(valor3))}\n")

def main():
    L = 0.0
    N = 0
    L_fixed = 0.0
    R = 0.0
    with open("../java/main/static.txt", 'r') as config_file:
        L = float(next(config_file))
        L_fixed = float(next(config_file))
        M = float(next(config_file))
        N = int(next(config_file))
        R = float(next(config_file))
        V = float(next(config_file))
    generar_archivos("../java/main/dynamic.txt","../java/main/dynamicOutput.txt", N, L_fixed, R, V)



if __name__ == "__main__":
    main()