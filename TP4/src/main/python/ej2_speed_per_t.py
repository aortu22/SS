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

def read_static_data(static_filename):
    # Read and extract wall length, number of particles, and frame distance
    # Return wall_length, num_particles, frame_distance
    with open(static_filename, 'r') as static_file:
        # Read the first line to get wall length
        R = float(static_file.readline().strip())
        static_file.readline().strip()
        L_fixed = float(static_file.readline().strip())

        N = int(static_file.readline().strip())

    return  N, R, L_fixed

def main():
    N_values = [5,10,15,20,25,30]
    config_path = "../java/main/static_2.txt"
    output_base_path = './'
    dt = 1.0E-3
    n, particleRadius, lineLength = read_static_data(config_path)
    phi_dt_difference = {}
    color_list = ['r', 'm', 'g', 'b', 'y', 'o']
    # color_list = ['navy', 'darkgreen', 'maroon', 'teal', 'gold', 'purple']


    for N in N_values:
        current_dt_particle_data = parse_output(output_base_path + "output_2_" + str(N) + "_" + str(dt) + ".txt")

        aux_vels = []
        for i in range(len(current_dt_particle_data)):
            vel_difference = 0

            for current_particle in current_dt_particle_data[i]:
                # print("Current particle id = " + str(current_particle['id']) + ", Next particle id = " + str(next_particle['id']))
                vel_difference += current_particle

            aux_vels.append(vel_difference/N)

        # plt.scatter([i*0.1 for i in range(0, 1801)], aux_vels, marker='o', linestyle='-', color=color_list[index-1],label=f'N= {N}')
        plt.plot([i * 0.1 for i in range(0, 1801)], aux_vels, linestyle='-', color=color_list[index - 1],label=f'N= {N}')

    plt.xlabel('Tiempo (s)')
    plt.ylabel('Velocidad Promedio ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.legend()
    plt.grid(True)
    # plt.savefig(f"graphs/ej_2_2_1_n_25_dt_{dt}.png")
    plt.savefig(f"graphs/ej_2_3_n_25_dt_{dt}.png")
    plt.cla()


if __name__ == "__main__":
    main()