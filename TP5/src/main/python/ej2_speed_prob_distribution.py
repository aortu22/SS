import matplotlib.pyplot as plt
import numpy as np
import math

def parse_output(filename):
    data = []
    current_time_data = []

    with open(filename, 'r') as output_file:
        lineas = output_file.readlines()
        for linea in lineas:
            if not linea.strip():  # Verificar si la línea está vacía
                continue  # Saltar a la siguiente iteración si la línea está vacía
            valores = linea.split()
            if len(valores) == 1:
                data.append(current_time_data)
                current_time_data = []
            elif len(valores) == 3:
                current_time_data.append(float(valores[2]))
    return data


def main():
    stationary = 120
    filenames = ['./output_2_10_0.001.txt',
                 './output_2_20_0.001.txt',
                 './output_2_30_0.001.txt']
    N = [10, 20, 30]
    velocities_dict = {}
    color_list = ['c', 'r', 'y']
    color_list_init = ['b', 'm', 'g']


    j = 0
    for filename in filenames:
        speed_matrix = parse_output(filename)
        velocities_dict[j] = []
        #Itero por cada arreglo de tiempos
        for i in range(len(speed_matrix)):
            if i > 120.00:
                break
            for current_particle in speed_matrix[i]:
                velocities_dict[j].append(current_particle)

        j += 1

    print(velocities_dict)

    plt.figure(figsize=(8, 6))  # Tamaño del gráfico
    for i in range(len(velocities_dict) - 1, -1, -1):
        velocities = velocities_dict[i]
        #Calculo al raiz de la cantidad de velocidades
        num_bins = int(np.sqrt(len(velocities)))
        hist, bin_edges = np.histogram(velocities, bins=num_bins, density=True)
        # hist, bin_edges = np.histogram(velocities, bins=20, density=True)

        bin_centers = 0.5 * (bin_edges[1:] + bin_edges[:-1])

        plt.plot(bin_centers, hist, linestyle='-', color=color_list[i], label=f'Stationary distribution N={N[i]}')

    # Agregar la recta y=1/3 desde el punto en el que la velocidad es 9
    x_recta = np.linspace(9, 12, 100)  # Valores de x desde 9 hasta el máximo
    y_recta = np.full_like(x_recta, 1 / 3)  # Array de 1/3 del mismo tamaño que x
    plt.plot(x_recta, y_recta, linestyle='dotted', color='b', label='Initial distribution')  # Agregar la recta

    plt.legend()  # Mostrar leyendas para cada conjunto de datos
    plt.xlabel('Velocidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.ylabel('Densidad de probabilidad ($\\frac{{\mathrm{1}}}{{\mathrm{cm/s}}})$')

    plt.grid(True)
    plt.legend()
    plt.show()



if __name__ == "__main__":
    main()