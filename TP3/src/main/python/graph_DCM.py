import numpy as np
import matplotlib.pyplot as plt


def main():

    deltaT = 0.01
    # Lista para almacenar las corridas
    corridas = []

    with open('tu_archivo.txt', 'r') as archivo:
        lineas = archivo.readlines()

    corrida_actual = []
    for i, linea in enumerate(lineas):
        if i % 2 == 1:
            valor = float(linea.strip())
            corrida_actual.append(valor)

    # Verificar si se completa una corrida (una línea en blanco)
    if not linea.strip():
        if corrida_actual:  # Ignorar si está vacía
            corridas.append(corrida_actual)
            corrida_actual = []

    # Añadir la última corrida si no se terminó con una línea en blanco
    if corrida_actual:
        corridas.append(corrida_actual)

    corridas_transpuestas = np.array(corridas).T




    medias = np.mean(corridas_transpuestas)

    desvios_std = np.std(corridas_transpuestas)

    x = [i * deltaT for i in range(len(corridas))]

    coefficients = np.polyfit(x,medias, 1)
    fit_line = np.poly1d(coefficients)
    y_fit = fit_line(x)
    # Graficar los datos como puntos
    plt.errorbar(x, medias, yerr=desvios_std, fmt='-o', capsize=5)
    plt.plot(x, y_fit, color='red', label='Ajuste Lineal')
    plt.xlabel('s')
    plt.ylabel('DCM')
    plt.title('Gráfico de DCM en el tiempo')
    plt.legend()
    plt.grid(True)
    plt.show()



if __name__ == "__main__":
    main()