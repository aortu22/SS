import matplotlib.pyplot as plt

def getErrors(filename):
    with open(filename, 'r') as output_file:
        lines = output_file.readlines()
        errors = 0
        i = 0
        n = 0
        while i < len(lines):
            time = float(lines[i].strip())
            i += 1

            frame_particles = []
            while i < len(lines) and lines[i].strip() != "":
                particle_data = lines[i].strip().split()
                x, error = map(float, particle_data)
                error += error
                n += 1
                i += 1

            i += 1  # Skip the empty line between frames

        return errors/n


def main():
    # Abre el archivo de texto para lectura
    #Un array con los nombres de todos los outputs a abrir
    filenames = ["output_beeman_1_0.01.txt", "output_verlet_1_0.01.txt", "output_gear_1_0.01.txt"]
    errors = []
    #Obtengo un array con los errores de cada prueba(con delta 1^-4,1^-6,etc
    for name in filenames:
        errors.append(getErrors(name))

    """"# Inicializa listas para almacenar los valores
    mse_output_verlet = []
    mse_output_beeman = []
    mse_output_gear = []

    # Itera a través de las líneas del archivo y procesa los valores
    for line in lines:
        values = line.strip().split('\t')
        if len(values) == 3:
            mse_output_verlet.append(float(values[0]))
            mse_output_beeman.append(float(values[1]))
            mse_output_gear.append(float(values[2]))
    """
    # Graficar los datos
    t = [10 ** -6, 10 ** -5, 10 ** -4, 10 ** -3, 10 ** -2]

    """plt.loglog(t, mse_output_verlet, linestyle='-', marker='o', label='Verlet')
    plt.loglog(t, mse_output_beeman, linestyle='-', marker='o', label='Beeman')
    plt.loglog(t, mse_output_gear, linestyle='-', marker='o', label='Gear Corrector Predictor')
    """
    plt.loglog(t, errors, linestyle='-', marker='o', label='METHOD_USED')

    plt.xlabel("Tiempo (s)", fontsize=15)
    plt.ylabel("MSE", fontsize=15)

    plt.legend()
    plt.tight_layout()
    plt.show()


if __name__ == '__main__':
    main()