import math

import matplotlib.pyplot as plt
import numpy as np

def parse_output(output_filename):
    with open(output_filename, 'r') as output_file:
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

        return positions,times, errors


def main():

    methods_to_use = ["Verlet", "Beeman", "Gear Predictor Corrector"]

    positions_beeman, times_beeman, errors_beeman = parse_output("output_beeman_1_0.01.txt")
    errors_beeman = sum(errors_beeman)/len(errors_beeman)
    positions_verlet, times_verlet, errors_verlet = parse_output("output_verlet_1_0.01.txt")
    errors_verlet = sum(errors_verlet)/len(errors_verlet)
    positions_gear, times_gear, errors_gear = parse_output("output_gear_1_0.01.txt")
    errors_gear = sum(errors_gear)/len(errors_gear)

    print("ECM Beeman ", errors_beeman)
    print("ECM Verlet ", errors_verlet)
    print("ECM Gear ", errors_gear)

    # Ejercicio 1.1 oscilator
    plt.plot(times_beeman, calculate_analytical_positions(np.array(times_beeman)), label="Analítica", linestyle='-')
    plt.plot(times_beeman, positions_beeman, label=f"{methods_to_use[1]}", linestyle= '--')
    plt.plot(times_verlet, positions_verlet, label=f"{methods_to_use[0]}", linestyle= '-.')
    plt.plot(times_gear, positions_gear, label=f"{methods_to_use[2]}", linestyle= ':')
    plt.xlabel("Tiempo (s)", fontsize=20)
    plt.ylabel("Posición (m)", fontsize=20)

    plt.tight_layout()
    plt.legend(loc="lower right")

    plt.show()


def calculate_analytical_positions(time_list):
    A = 1.0
    k = 10000
    mass = 70.0
    gamma = 100.0

    return A * np.exp(-gamma*time_list/(2*mass)) * np.cos(math.sqrt(k/mass - gamma**2/(4*mass**2))*time_list)


if __name__ == '__main__':
    main()