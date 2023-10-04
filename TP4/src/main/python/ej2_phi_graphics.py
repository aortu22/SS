import matplotlib.pyplot as plt


def get_static_data(config_path):
    with open(config_path, 'r') as config_file:
        R = float(next(config_file))
        M = float(next(config_file))
        L = float(next(config_file))
        N = int(next(config_file))
        return R, M, L, N


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
                current_time_data.append(float(valores[0]))
    return data


def main():
    config_path = "../java/main/static_2.txt"
    output_base_path = './'
    dt_values = [1.0E-1, 1.0E-2, 1.0E-3, 1.0E-4, 1.0E-5]
    dt_values_str = ["0.1", "0.01", "0.001", "1.0E-4", "1.0E-5"]
    R, M, L, N = get_static_data(config_path)
    phi_dt_difference = {}
    color_list = ['b', 'g', 'r', 'c']

    index = 1
    for dt in range(len(dt_values) - 1):
        current_dt = dt_values_str[dt]
        next_dt = dt_values_str[dt + 1]
        current_dt_particle_data = parse_output(output_base_path + "output_2_25_" + current_dt + ".txt")
        next_dt_particle_data = parse_output(output_base_path + "output_2_25_" + next_dt + ".txt")

        # Diferencia entre current y next
        aux_phi = []
        for i in range(len(current_dt_particle_data)):
            x_difference = 0
            for current_particle, next_particle in zip(current_dt_particle_data[i], next_dt_particle_data[i]):
                dist_directa = abs(next_particle - current_particle)
                x_difference += min(dist_directa, L - dist_directa)
            aux_phi.append(x_difference)

        phi_dt_difference[index] = aux_phi
        numbers = [i * 0.1 for i in range(0, 1801)]
        plt.plot(numbers, aux_phi, linestyle='-', color=color_list[index - 1], label=f'K= {index}')
        index += 1

    plt.xlabel('Tiempo (s)')
    plt.ylabel('Φ(t)')
    plt.xlim(0, 180)
    plt.legend()
    plt.show()

    plt.cla()


if __name__ == "__main__":
    main()
