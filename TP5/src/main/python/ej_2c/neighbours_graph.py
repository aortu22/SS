import matplotlib.pyplot as plt
import numpy as np


def find_local_minima(arr):
    minima_indexes = []
    for i in range(1, len(arr) - 1):
        if arr[i] < arr[i - 1] and arr[i] < arr[i + 1]:
            minima_indexes.append(i)
    return minima_indexes

def main():
    with open('./output_pedestrian_neighbours.txt', 'r') as archivo:
        lineas = archivo.readlines()
    times = []
    dMin_arr = []
    ids = []
    dMin = 0
    amount = 0

    # Custom color list
    colors = ['g', 'r', 'c', 'm', 'y', 'k', 'purple', 'navy', 'lime', 'gold', 'purple', 'brown', 'pink', 'orange', 'teal', 'olive', 'indigo', 'silver', 'darkgreen', 'sienna', 'tomato', 'orchid', 'deepskyblue', 'cornflowerblue', 'b']

    # Iterate over lines and extract data
    for linea in lineas:
        if not linea.strip():  # Skip empty lines
            continue
        palabras = linea.split()
        t = float(palabras[0])
        d = float(palabras[1])
        id = float(palabras[2])
        times.append(t)
        dMin_arr.append(d)
        ids.append(id)
        dMin += d
        amount += 1

    print(dMin/amount)

    minima_indexes = find_local_minima(dMin_arr)

    # Create the line plot
    plt.plot(times, dMin_arr, linestyle='-')

    used_ids = []
    for i in minima_indexes:
        if ids[i] in used_ids:
            print('')
            plt.plot(times[i], dMin_arr[i], marker='.', color=colors[int(ids[i] % len(colors))])
        else:
            plt.plot(times[i], dMin_arr[i], marker='.', color=colors[int(ids[i] % len(colors))])
            used_ids.append(ids[i])

    # Add labels and title
    plt.xlabel('Tiempo [s]')
    plt.ylabel('d_Min [m]')
    plt.grid()

    # Personalizar la leyenda
    # legend_labels = [f'ID={id}' for id in used_ids]
    # legend_markers = [plt.Line2D([0], [0], marker='.', color='w', markersize=8, markerfacecolor=colors[i % len(colors)]) for i in range(len(legend_labels))]
    # legend = plt.legend(legend_markers, legend_labels, loc='upper right', title='Labels', markerscale=1, fontsize=8)
    # plt.setp(legend.get_title(), fontsize=10)

    plt.show()

    minima_values = [dMin_arr[i] for i in minima_indexes]

    print(minima_values)
    print(sum(minima_values)/len(minima_values))
    print(np.std(minima_values))

    print(min(dMin_arr))

if __name__ == '__main__':
    main()
