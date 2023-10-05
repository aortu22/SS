import random
from particle import Particle

def checkNewParticle(particle_created,x,R):
    for particle in particle_created:
        if abs(x - particle.getX()) <= (R * 2):
            return False
    return True



def generar_archivos(nombre_archivo, nombreDynamicOutput, n, L, R, u_array):
    # espacios = L / (R * 2)
    espacios = [i for i in range(30)]
    print(n)
    with open(nombre_archivo, 'w') as f:
        with open(nombreDynamicOutput, 'w') as fOut:
            f.write("0\n")
            fOut.write("0\n")

            particle_created = []
            for i in range(n):
                index = random.randint(0, len(espacios) - 1)
                valorX = round(espacios[index] * (2 * R), 6)
                espacios.pop(index)
                # while not checkNewParticle(particle_created, valorX, R):
                #     valorX = round(random.randint(0, len(espacios)) * (2 * R), 6)
                particle = Particle(i)
                particle.set_postion(valorX, 0)
                particle_created.append(particle)
                f.write(f"{valorX} {valorX} {u_array[i]}\n")
                fOut.write(f"{valorX} {valorX} {u_array[i]}\n")

def complete_static( N ):
    u_array = []
    for i in range(N):
        valorU = round(random.uniform(9, 12), 6)
        u_array.append(valorU)
    return u_array


def main():
    M = 0.0
    R = 0.0
    L = 0.0
    N = 0
    with open("../java/main/static_2.txt", 'r') as config_file:
        R = float(next(config_file))
        M = float(next(config_file))
        L = float(next(config_file))
        N = int(next(config_file))
        dT = float(next(config_file))
        dTEscritura = float(next(config_file))
    u_array = complete_static(N)
    generar_archivos("../java/main/dynamic_2.txt", "../java/main/dynamicOutput_2.txt", N, L, R, u_array)


if __name__ == "__main__":
    main()