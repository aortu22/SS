import random

def generar_archivo(nombre_archivo, n, L):
    with open(nombre_archivo, 'w') as f:
        f.write("0\n")
        for _ in range(n):
            valor1 = round(random.uniform(0, L), 2)
            valor2 = round(random.uniform(0, L), 2)
            valor3 = round(random.uniform(0, 360), 2)
            f.write(f"{valor1} {valor2} {valor3}\n")

def main():
    L = 0.0
    N = 0
    with open("../java/main/static.txt", 'r') as config_file:
        L = float(next(config_file))
        N = int(next(config_file))
    generar_archivo("../java/main/dynamic.txt",N,L)

    destination_file_path = "../java/main/dynamicOutput.txt"  # Replace with the desired path for the destination file
    source_file_path = "../java/main/dynamic.txt"  # Replace with the actual path to the source file

    with open(source_file_path, "r") as source_file:
        content = source_file.read()
    with open(destination_file_path, "w") as destination_file:
        destination_file.write(content)


if __name__ == "__main__":
    main()