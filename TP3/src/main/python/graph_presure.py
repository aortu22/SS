import matplotlib.pyplot as plt
import numpy as np


def get_perimeter_l():
    with open("../java/main/static.txt", 'r') as config_file:
        L = float(next(config_file))
        L_fixed = float(next(config_file))
        total_perimeter = L_fixed * 3 + (L_fixed - L)
        return total_perimeter

def get_perimeter_r():
    with open("../java/main/static.txt", 'r') as config_file:
        L = float(next(config_file))
        L_fixed = float(next(config_file))
        total_perimeter = L_fixed * 2 + L
        return total_perimeter

def get_perimeter():
    with open("../java/main/static.txt", 'r') as config_file:
        L = float(next(config_file))
        L_fixed = float(next(config_file))
        total_perimeter = L_fixed * 5 + (L_fixed-L) + L
        return total_perimeter

def main():

    # impulse_file = './impulse0.03.txt'
    # impulse_file = './impulse0.05.txt'
    # impulse_file = './impulse0.07.txt'
    impulse_file = './impulse0.09.txt'


    times = [i for i in range(100)]

    #longitud total del perimetro
    perimeter_l = get_perimeter_l()
    perimeter_r = get_perimeter_r()
    perimeter = get_perimeter()

    # Lista para almacenar las corridas de 0.03
    corridas_r = np.zeros((5, len(times)), dtype=float)
    corridas_l = np.zeros((5, len(times)), dtype=float)
    corridas = np.zeros((5, len(times)), dtype=float)

    with open(impulse_file, 'r') as impulse_file:
        i = 0
        while i < 5:
            prev = ''
            impulse_file.readline() # Jump L = 0.03 in file
            while True:
                line = impulse_file.readline()

                if not line:  # If readline() returns an empty string, we've reached the end of the file
                    break
                if line == "\n":
                    if prev != "\n":
                        break
                else:
                    tiempo, presion_l, presion_r = map(float, line.strip().split())
                    segundos = int(tiempo)
                    corridas_r[i, segundos - 1] += float(presion_r)
                    corridas_l[i, segundos - 1] += float(presion_l)
                    corridas[i, segundos - 1] += (float(presion_l) + float(presion_r))

                prev = line

            corridas_r[i] = corridas_r[i] / perimeter_r
            corridas_l[i] = corridas_l[i] / perimeter_l
            corridas[i] = corridas[i] / perimeter
            i += 1

    corridas_transpuestas_r = np.array(corridas_r).T
    mean_corridas_003_r = []
    std_corridas_003_r = []
    for corrida in corridas_transpuestas_r:
        mean_corridas_003_r.append(np.mean(corrida))
        std_corridas_003_r.append(np.std(corrida))

    corridas_transpuestas_l = np.array(corridas_l).T
    mean_corridas_003_l = []
    std_corridas_003_l = []
    for corrida in corridas_transpuestas_l:
        mean_corridas_003_l.append(np.mean(corrida))
        std_corridas_003_l.append(np.std(corrida))


    plt.figure(figsize=(10, 6))


    direct = ['Izquierda', 'Derecha']


    # Create lines for the vectors
    plt.errorbar(times, np.array(mean_corridas_003_l), yerr=np.array(std_corridas_003_l), fmt='-o', label=f"Recinto = {direct[0]}")
    plt.errorbar(times, np.array(mean_corridas_003_r), yerr=np.array(std_corridas_003_r), fmt='-o', label=f"Recinto = {direct[1]}")

    # Dividir todos los valores de presión por x
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Presión (Pa)')
    plt.grid(True)
    plt.legend()
    plt.show()



if __name__ == "__main__":
    main()