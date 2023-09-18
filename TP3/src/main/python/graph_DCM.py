import numpy as np
import matplotlib.pyplot as plt


def main():

    deltaT = 0.1
    # Lista para almacenar las corridas
    corridas = []
    i = 0
    with open('./outputDCM.txt', 'r') as archivo:
        while i < 5:
            prev = ''
            archivo.readline() # Jump L = 0.03 in file
            corrida_actual = []
            while True:
                linea = archivo.readline()

                if not linea:  # If readline() returns an empty string, we've reached the end of the file
                    break
                if linea == "\n":
                    if prev != "\n":
                        break
                else:
                    t, valor = map(float, linea.strip().split())
                    corrida_actual.append(4 * valor * t)
            corridas.append(corrida_actual)
            i += 1

    min_length = 10000000000
    i = 0
    for corrida in corridas:
        if min_length > len(corrida):
            min_length = len(corrida)

    j = 0
    while j < 5:
        corridas[j] = corridas[j][:min_length]
        j += 1



    corridas_transpuestas = np.array(corridas).T
    print(corridas)
    print(corridas_transpuestas)
    mean_corridas = []
    std_corridas = []
    for corrida in corridas_transpuestas:
        mean_corridas.append(np.mean(corrida))
        std_corridas.append(np.std(corrida))

    x = [i * deltaT for i in range(len(corridas[0]))]

    coefficients = np.polyfit(x, mean_corridas, 1)
    fit_line = np.poly1d(coefficients)
    y_fit = fit_line(x)
    # Graficar los datos como puntos
    plt.errorbar(x, mean_corridas, yerr=std_corridas, fmt='-o', capsize=5)
    plt.plot(x, y_fit, color='red', label='Ajuste Lineal')
    plt.xlabel('s')
    plt.ylabel('DCM')
    plt.title('Gráfico de DCM en el tiempo')
    plt.legend()
    plt.grid(True)
    plt.show()



if __name__ == "__main__":
    main()