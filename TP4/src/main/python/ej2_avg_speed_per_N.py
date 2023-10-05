import matplotlib.pyplot as plt

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
            elif len(valores) == 2:
                current_time_data.append(float(valores[2]))
    return data


def main():
    N_values = [5, 10, 15, 20, 25, 30]
    dt_values = [1.0E-3]
    phi_dt_difference = {}

    for dt in dt_values:
        index = 0
        for N in N_values:
            speed_matrix = parse_output("./output_2_" + str(N) + "_0.001" + ".txt")

            #Arreglo con el promedio de velocidades por tiempo
            aux_vels = []
            #Itero por cada arreglo de tiempos
            for i in range(len(speed_matrix)):
                if i < 120.00:
                   continue
                vel_difference = 0

                for current_particle in speed_matrix[i]:
                    vel_difference += current_particle

                aux_vels.append(vel_difference/N)

            #Acumulo todas los promedio de las velocidades por cada simulacion agrupando por N
            phi_dt_difference[N] = aux_vels
            index += 1

    # Calcular el promedio para cada valor de N en phi_dt_difference
    promedio_vel_dict = {N: sum(vels) / len(vels) for N, vels in phi_dt_difference.items()}

    # Crear listas separadas para el eje X y el eje Y
    N_list = list(promedio_vel_dict.keys())
    promedio_vel_list = list(promedio_vel_dict.values())

    aux_prom = []
    for i in range(len(N_values)):
        aux_prom.append(promedio_vel_list[i])

    # Dibujar un punto por cada valor de N en el gráfico de dispersión
    plt.scatter(N_list, aux_prom, marker='o', color='b', label='Puntos')

    # Unir los puntos con una línea
    error = [0.01, 0.03, 0.05, 0.02, 0.02, 0.2]
    plt.plot(N_list, aux_prom, linestyle='-', color='g', label='Línea de Unión')
    plt.errorbar(N_list, aux_prom, yerr=error, fmt='o', capsize=6)

    plt.xlabel('N')
    plt.ylabel('Velocidad Promedio ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.grid(True)
    plt.show()


if __name__ == "__main__":
    main()