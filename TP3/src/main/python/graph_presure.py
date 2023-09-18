import matplotlib.pyplot as plt
import numpy as np


def main():

    impulse_file = './impulse0.03.txt'
    # impulse_file = './impulse0.05.txt'
    # impulse_file = './impulse0.07.txt'
    # impulse_file = './impulse0.09.txt'

    time = 40
    times = [i for i in range(time)]
    runs = 5
    # Lista para almacenar las corridas
    corridas_r = np.zeros((runs, len(times)), dtype=float)
    corridas_l = np.zeros((runs, len(times)), dtype=float)

    with open(impulse_file, 'r') as impulse_file:
        impulse_file.readline() # Jump first enter
        i = 0
        while i < runs:
            prev = ''
            impulse_file.readline() # Jump L = x in file
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

                prev = line
            i += 1

    corridas_transpuestas_r = np.array(corridas_r).T
    mean_corridas_r = []
    std_corridas_r = []
    for corrida in corridas_transpuestas_r:
        mean_corridas_r.append(np.mean(corrida))
        std_corridas_r.append(np.std(corrida))

    corridas_transpuestas_l = np.array(corridas_l).T
    mean_corridas_l = []
    std_corridas_l = []
    for corrida in corridas_transpuestas_l:
        mean_corridas_l.append(np.mean(corrida))
        std_corridas_l.append(np.std(corrida))


    plt.figure(figsize=(10, 6))
    direct = ['Izquierda', 'Derecha']
    # Create lines for the vectors
    plt.errorbar(times, np.array(mean_corridas_l), yerr=np.array(std_corridas_l), fmt='-o', label=f"Recinto = {direct[0]}")
    plt.errorbar(times, np.array(mean_corridas_r), yerr=np.array(std_corridas_r), fmt='-o', label=f"Recinto = {direct[1]}")

    # Dividir todos los valores de presión por x
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Presión (Kg/s^2)')
    plt.grid(True)
    plt.legend()
    plt.show()


if __name__ == "__main__":
    main()