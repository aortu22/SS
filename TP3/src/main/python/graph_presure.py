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
    corridas_003_r = []
    corridas_003_l = []
    with open(impulse_003_file, 'r') as impulse_003_file:
        prev = ''
        impulse_003_file.readline()  # Jump L = 0.03 in file
        presiones_por_segundo_l = {}
        presiones_por_segundo_r = {}
        while True:
            line = impulse_003_file.readline()
            tiempo, presion_l, presion_r = map(float, line.strip().split())
            segundo = int(tiempo)

            if not line:  # If readline() returns an empty string, we've reached the end of the file
                break
            if line == "\n":
                if prev != "\n":
                    break
            else:
                # Verificar si ya existe una suma para ese segundo en el diccionario
                if segundo in presiones_por_segundo_r:
                    presiones_por_segundo_r[segundo] += presion_r
                    presiones_por_segundo_l[segundo] += presion_l
                else:
                    presiones_por_segundo_r[segundo] = presion_r
                    presiones_por_segundo_l[segundo] = presion_l
            prev = line

        for tiempo, presion in presiones_por_segundo_r.items():
            presiones_por_segundo_r[tiempo] = presiones_por_segundo_r[tiempo] / perimeter_length

        for tiempo, presion in presiones_por_segundo_l.items():
            presiones_por_segundo_l[tiempo] = presiones_por_segundo_l[tiempo] / perimeter_length

        corridas_003_r.append(presiones_por_segundo_r)
        corridas_003_l.append(presiones_por_segundo_l)

    mean_corridas_003_r = []
    std_corridas_003_r = []
    for corridas in corridas_003_r:
        mean_corridas_003_r.append(np.mean(list(corridas.values())))
        std_corridas_003_r.append(np.std(list(corridas.values())))

    mean_corridas_003_l = []
    std_corridas_003_l = []
    for corridas in corridas_003_l:
        mean_corridas_003_l.append(np.mean(list(corridas.values())))
        std_corridas_003_l.append(np.std(list(corridas.values())))



    plt.figure(figsize=(10, 6))


    # Create lines for the vectors
    plt.errorbar(len(mean_corridas_003_l), mean_corridas_003_l, yerr=std_corridas_003_l, fmt='-o', label="L=0.03")
    plt.errorbar(len(mean_corridas_003_r), mean_corridas_003_r, yerr=std_corridas_003_r, fmt='-o', label="L=0.03")

# Create boxes of info for N values
    direct = ['Izquierda', 'Derecha']
    for d in direct:
        plt.text(5.1, 0.25, f"Recinto={d}", verticalalignment="center")


    # Dividir todos los valores de presión por x
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Presión (Pa)')
    plt.title('Presión por segundo')
    plt.grid(True)
    plt.show()



if __name__ == "__main__":
    main()