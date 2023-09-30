import numpy as np
import matplotlib.pyplot as plt


def main():

    # outputDCM_file = './outputDCM0.03.txt'
    # outputDCM_file = './outputDCM0.05.txt'
    # outputDCM_file = './outputDCM0.07.txt'
    outputDCM_file = './outputDCM0.09.txt'

    deltaT = 2
    # Lista para almacenar las corridas
    corridas = []
    runs = 5
    i = 0
    with open(outputDCM_file, 'r') as archivo:
        archivo.readline() # Jump first enter
        while i < runs:
            prev = ''
            archivo.readline() # Jump L = x in file
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
                    corrida_actual.append(valor)
            corridas.append(corrida_actual)
            i += 1

    min_length = 10000000000
    i = 0
    for corrida in corridas:
        if min_length > len(corrida):
            min_length = len(corrida)

    j = 0
    while j < runs:
        corridas[j] = corridas[j][:min_length]
        j += 1



    corridas_transpuestas = np.array(corridas).T
    mean_corridas = []
    std_corridas = []
    for corrida in corridas_transpuestas:
        mean_corridas.append(np.mean(corrida))
        std_corridas.append(np.std(corrida))

    x = [i * deltaT for i in range(len(corridas[0]))]
    short_x = x[:10]


    coefficients = np.polyfit(short_x, mean_corridas[:10], 1)
    fit_line = np.poly1d(coefficients)
    y_fit = fit_line(short_x)

    # Graficar los datos como puntos
    plt.errorbar(x, mean_corridas, yerr=std_corridas, fmt='-o', capsize=5)
    plt.plot(short_x, y_fit, color='red', label='Ajuste Lineal')
    plt.xlabel('Tiempo (s)')
    plt.ylabel('DCM')
    # plt.title('Gráfico de DCM en el tiempo')
    plt.legend()
    plt.grid(True)
    plt.show()



if __name__ == "__main__":
    main()