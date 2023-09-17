import matplotlib.pyplot as plt
import numpy as np


def get_perimeter_length():
    with open("../java/main/static.txt", 'r') as config_file:
        L = float(next(config_file))
        L_fixed = float(next(config_file))
        total_perimeter = L_fixed * 3 + (L_fixed - L) + L_fixed *2 + L
        return total_perimeter

def main():

    impulse_003_file = './impulse0.03.txt'
    impulse_005_file = './impulse0.05.txt'
    impulse_007_file = './impulse0.07.txt'
    impulse_009_file = './impulse0.09.txt'


    L = [0.03, 0.05, 0.07, 0.09]

    #longitud total del perimetro
    perimeter_length = get_perimeter_length()

    # Lista para almacenar las corridas de 0.03
    corridas_003 = []
    with open(impulse_003_file, 'r') as impulse_003_file:
        prev = ''
        impulse_003_file.readline()  # Jump L = 0.03 in file
        presiones_por_segundo = {}
        while True:
            line = impulse_003_file.readline()
            tiempo, presion = map(float, line.strip().split())
            segundo = int(tiempo)

            if not line:  # If readline() returns an empty string, we've reached the end of the file
                break
            if line == "\n":
                if prev != "\n":
                    break
            else:
                # Verificar si ya existe una suma para ese segundo en el diccionario
                if segundo in presiones_por_segundo:
                    presiones_por_segundo[segundo] += presion
                else:
                    presiones_por_segundo[segundo] = presion
            prev = line

        for tiempo, presion in presiones_por_segundo.items():
            presiones_por_segundo[tiempo] = presiones_por_segundo[tiempo] / perimeter_length

        corridas_003.append(presiones_por_segundo)

    mean_corridas_003 = []
    std_corridas_003 = []
    for corridas in corridas_003:
        mean_corridas_003.append(np.mean(list(corridas.values())))
        std_corridas_003.append(np.std(list(corridas.values())))

    # Lista para almacenar las corridas de 0.05
    corridas_005 = []
    with open(impulse_005_file, 'r') as impulse_005_file:
        prev = ''
        impulse_005_file.readline()  # Jump L = 0.05 in file
        presiones_por_segundo = {}
        while True:
            line = impulse_005_file.readline()
            tiempo, presion = map(float, line.strip().split())
            segundo = int(tiempo)

            if not line:  # If readline() returns an empty string, we've reached the end of the file
                break
            if line == "\n":
                if prev != "\n":
                    break
            else:
                # Verificar si ya existe una suma para ese segundo en el diccionario
                if segundo in presiones_por_segundo:
                    presiones_por_segundo[segundo] += presion
                else:
                    presiones_por_segundo[segundo] = presion
            prev = line

        for tiempo, presion in presiones_por_segundo.items():
            presiones_por_segundo[tiempo] = presiones_por_segundo[tiempo] / perimeter_length

        corridas_005.append(presiones_por_segundo)

    mean_corridas_005 = []
    std_corridas_005 = []
    for corridas in corridas_005:
        mean_corridas_005.append(np.mean(list(corridas.values())))
        std_corridas_005.append(np.std(list(corridas.values())))


    # Lista para almacenar las corridas de 0.07
    corridas_007 = []
    with open(impulse_007_file, 'r') as impulse_007_file:
        prev = ''
        impulse_007_file.readline()  # Jump L = 0.07 in file
        presiones_por_segundo = {}
        while True:
            line = impulse_007_file.readline()
            tiempo, presion = map(float, line.strip().split())
            segundo = int(tiempo)

            if not line:  # If readline() returns an empty string, we've reached the end of the file
                break
            if line == "\n":
                if prev != "\n":
                    break
            else:
                # Verificar si ya existe una suma para ese segundo en el diccionario
                if segundo in presiones_por_segundo:
                    presiones_por_segundo[segundo] += presion
                else:
                    presiones_por_segundo[segundo] = presion
            prev = line

        for tiempo, presion in presiones_por_segundo.items():
            presiones_por_segundo[tiempo] = presiones_por_segundo[tiempo] / perimeter_length

        corridas_007.append(presiones_por_segundo)

    mean_corridas_007 = []
    std_corridas_007 = []
    for corridas in corridas_007:
        mean_corridas_007.append(np.mean(list(corridas.values())))
        std_corridas_007.append(np.std(list(corridas.values())))


    # Lista para almacenar las corridas de 0.09
    corridas_009 = []
    with open(impulse_009_file, 'r') as impulse_009_file:
        prev = ''
        impulse_009_file.readline()  # Jump L = 0.09 in file
        presiones_por_segundo = {}
        while True:
            line = impulse_009_file.readline()
            tiempo, presion = map(float, line.strip().split())
            segundo = int(tiempo)

            if not line:  # If readline() returns an empty string, we've reached the end of the file
                break
            if line == "\n":
                if prev != "\n":
                    break
            else:
                # Verificar si ya existe una suma para ese segundo en el diccionario
                if segundo in presiones_por_segundo:
                    presiones_por_segundo[segundo] += presion
                else:
                    presiones_por_segundo[segundo] = presion
            prev = line

        for tiempo, presion in presiones_por_segundo.items():
            presiones_por_segundo[tiempo] = presiones_por_segundo[tiempo] / perimeter_length

        corridas_009.append(presiones_por_segundo)

    mean_corridas_009 = []
    std_corridas_009 = []
    for corridas in corridas_009:
        mean_corridas_009.append(np.mean(list(corridas.values())))
        std_corridas_009.append(np.std(list(corridas.values())))



    plt.figure(figsize=(10, 6))


    # Create lines for the vectors
    plt.errorbar(len(mean_corridas_003), mean_corridas_003, yerr=std_corridas_003, fmt='-o', label="L=0.03")
    plt.errorbar(len(mean_corridas_005), mean_corridas_005, yerr=std_corridas_005, fmt='-o', label="L=0.05")
    plt.errorbar(len(mean_corridas_007), mean_corridas_007, yerr=std_corridas_007, fmt='-o', label="L=0.07")
    plt.errorbar(len(mean_corridas_009), mean_corridas_009, yerr=std_corridas_009, fmt='-o', label="L=0.09")

    # Create boxes of info for N values
    for l in L:
        plt.text(5.1, 0.25 * l, f"L={l}", verticalalignment="center")


# Dividir todos los valores de presión por x
    presiones_divididas = [presion / perimeter_length for presion in presiones]
    plt.bar(tiempos, presiones_divididas, width=1.0, align='edge')
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Presión (Pa)')
    plt.title('Presión por segundo')
    plt.grid(True)
    plt.show()



if __name__ == "__main__":
    main()