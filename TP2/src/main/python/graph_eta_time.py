import matplotlib.pyplot as plt
import numpy as np

# Este .py se usa para ver en que tiempo se normaliza el Va
# y ponerlo en hraph_eta para el calculo de la media y el desvio estandar
def main():
    density = 0.16
    n_file = './order40.txt'

    eta = [0.0, 2.0, 4.0]

    n_vec = np.zeros((len(eta), 4001), dtype=float)

    with open(n_file, 'r') as n_file:
        prev = ''
        i = 0
        while i < len(eta):
            j = 0
            n_file.readline()  # Jump n = eta in file
            while True:
                line = n_file.readline()
                if not line:  # If readline() returns an empty string, we've reached the end of the file
                    break
                if line == "\n":
                    if prev != "\n":
                        break
                else:
                    n_vec[i, j] = float(line[:-1].replace(",", "."))
                j += 1
                prev = line
            i += 1
    plt.figure(figsize=(10, 6))
    i = 0
    numbers = [i for i in range(4001)]

    while i < len(eta):
        label = "η=" + str(eta[i])
        plt.plot(numbers, n_vec[i], label=label)
        i += 1

    # # Create boxes of info for N values
    # for n in eta:
    #     plt.text(5.1, 0.25 * n, f"η={n}", verticalalignment="center")

    # Customize the plot
    plt.ylabel("Va")
    plt.xlabel("Iteraciones")
    # plt.title("Time and Va variation according to η with density " + str(density))
    plt.ylim(0, 1)
    plt.xlim(0, 4001)
    plt.legend()

    # Show the plot
    plt.show()


if __name__ == "__main__":
    main()
