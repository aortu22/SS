import random
from particle import Particle

def checkNewParticle(particle_created,x,R):
    for particle in particle_created:
        if (x - particle.getX()) <= (R * 2):
            return False
    return True


def generar_archivos(nombre_archivo, nombreDynamicOutput, n, L, R):
    print(n)
    with open(nombre_archivo, 'w') as f:
        with open(nombreDynamicOutput, 'w') as fOut:
            f.write("0\n")
            fOut.write("0\n")
            particle_created = []
            for _ in range(n):
                valorX = round(random.uniform(0 + R*1.5, L - R*1.5), 6)
                while not checkNewParticle(particle_created, valorX, R):
                    valorX = round(random.uniform(0 + R*1.5, L - R*1.5), 6)
                particle = Particle(_)
                particle.set_postion(valorX, 0)
                particle_created.append(particle)
                f.write(f"{valorX}\n")
                fOut.write(f"{valorX}\n")

def complete_static(nombre_archivo, M, R, L, N,dT, dTEscritura):
    with open(nombre_archivo, 'w') as f:
        f.write(f"{R}\n")
        f.write(f"{M}\n")
        f.write(f"{L}\n")
        f.write(f"{N}\n")
        f.write(f"{dT}\n")
        f.write(f"{dTEscritura}\n")

        for _ in range(N):
            valorU = round(random.uniform(9,12), 6)
            f.write(f"{valorU}\n")


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
    complete_static("../java/main/static_2.txt", M, R, L, N,dT, dTEscritura)
    generar_archivos("../java/main/dynamic_2.txt", "../java/main/dynamicOutput_2.txt", N, L, R)



if __name__ == "__main__":
    main()