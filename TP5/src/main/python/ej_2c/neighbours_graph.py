import matplotlib.pyplot as plt
def main():
    with open('./output_pedestrian_neighbours.txt', 'r') as archivo:
        lineas = archivo.readlines()
    times = []
    dMin_arr = []
    dMin = 0
    amount = 0

    # Iterar sobre las líneas y extraer las velocidades
    for linea in lineas:
        if not linea.strip():  # Skip empty lines
            continue
        palabras = linea.split()
        t = float(palabras[0])
        d = float(palabras[1])
        times.append(t)
        dMin_arr.append(d)
        dMin += d
        amount += 1

    print(dMin/amount)

    # Create a line plot
    plt.plot(times, dMin_arr, marker='o', linestyle='-')

    # Add labels and title
    plt.xlabel('Tiempo [s]')
    plt.ylabel('d_Min [m]')

    plt.show()
if __name__ == '__main__':
    main()
