import matplotlib.pyplot as plt

def getErrors(filename):
    with open(filename, 'r') as output_file:
        lines = output_file.readlines()
        times = []
        positions = []
        errors = []
        i = 0
        while i < len(lines):
            time = float(lines[i].strip())
            i += 1

            frame_particles = []
            while i < len(lines) and lines[i].strip() != "":
                particle_data = lines[i].strip().split()
                x, error = map(float, particle_data)
                times.append(time)
                positions.append(x)
                errors.append(error)
                i += 1

            i += 1  # Skip the empty line between frames

        return errors


def main():
    # Abre el archivo de texto para lectura
    #Un array con los nombres de todos los outputs a abrir
    beeman_filenames = ["output_beeman_1_0.01.txt", "output_beeman_1_0.001.txt", "output_beeman_1_1.0E-4.txt", "output_beeman_1_1.0E-5.txt", "output_beeman_1_1.0E-6.txt"]
    beeman_errors = []
    #Obtengo un array con los errores de cada prueba(con delta 1^-4,1^-6,etc
    for name in beeman_filenames:
        beeman_err = getErrors(name)
        beeman_errors.append(sum(beeman_err)/len(beeman_err))

    gear_filenames = ["output_gear_1_0.01.txt", "output_gear_1_0.001.txt", "output_gear_1_1.0E-4.txt", "output_gear_1_1.0E-5.txt", "output_gear_1_1.0E-6.txt"]
    gear_errors = []
    #Obtengo un array con los errores de cada prueba(con delta 1^-4,1^-6,etc
    for name in gear_filenames:
        gear_err = getErrors(name)
        gear_errors.append(sum(gear_err)/len(gear_err))

    verlet_filenames = ["output_verlet_1_0.01.txt", "output_verlet_1_0.001.txt", "output_verlet_1_1.0E-4.txt", "output_verlet_1_1.0E-5.txt", "output_verlet_1_1.0E-6.txt"]
    verlet_errors = []
    #Obtengo un array con los errores de cada prueba(con delta 1^-4,1^-6,etc
    for name in verlet_filenames:
        verlet_err = getErrors(name)
        verlet_errors.append(sum(verlet_err)/len(verlet_err))

    print(beeman_errors)
    print(verlet_errors)
    print(gear_errors)

    # Graficar los datos
    t = [10 ** -2, 10 ** -3, 10 ** -4, 10 ** -5, 10 ** -6]

    plt.loglog(t, verlet_errors, linestyle='-.', marker='o', label='Verlet')
    plt.loglog(t, beeman_errors, linestyle='--', marker='o', label='Beeman')
    plt.loglog(t, gear_errors, linestyle=':', marker='o', label='Gear Corrector Predictor')



    plt.xlabel("$\Delta t$ ($s$)", fontsize=15)
    plt.ylabel("MSE", fontsize=15)

    plt.legend()
    plt.tight_layout()
    plt.show()


if __name__ == '__main__':
    main()