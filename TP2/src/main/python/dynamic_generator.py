import random

def generar_archivo(nombre_archivo, n, L):
    with open(nombre_archivo, 'w') as f:
        f.write("0\n")
        for _ in range(n):
            valor1 = round(random.uniform(0, L), 2)
            valor2 = round(random.uniform(0, L), 2)
            f.write(f"{valor1} {valor2}\n")

def main():
    L = 0.0
    N = 0
    with open("../java/main/static.txt", 'r') as config_file:
        L = float(next(config_file))
        N = int(next(config_file))
    generar_archivo("../java/main/dynamic.txt",N,L)
    

if __name__ == "__main__":
    main()