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
            elif len(valores) == 3:
                current_time_data.append(float(valores[2]))
    return data

def main():
    N_values = [5, 10, 15, 20, 25, 30]
    dt = 0.001
    phi_dt_difference = {}
    color_list = ['r', 'm', 'g', 'b', 'y', 'c']
    # color_list = ['navy', 'darkgreen', 'maroon', 'teal', 'gold', 'purple']

    index = 0
    for N in N_values:
        current_dt_particle_data = parse_output("./output_2_" + str(N) + "_" + str(dt) + ".txt")

        aux_vels = []
        for i in range(len(current_dt_particle_data)):
            vel_difference = 0

            for current_particle in current_dt_particle_data[i]:
                vel_difference += current_particle

            aux_vels.append(vel_difference/N)

        plt.plot([i * 0.1 for i in range(0, 1801)], aux_vels, linestyle='-', color=color_list[index], label=f'N= {N}')
        index += 1

    plt.xlabel('Tiempo (s)')
    plt.ylabel('Velocidad Promedio ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.legend()
    plt.grid(True)
    plt.show()

if __name__ == "__main__":
    main()