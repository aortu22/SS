import matplotlib.pyplot as plt
import numpy as np


def main():
    density = 0.16
    n100_file = './order100.txt'

    eta = [0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9,
           1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9,
           2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9,
           3.0, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9,
           4.0, 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 5.0]

    n_vec = np.zeros((len(eta), 1501), dtype=float)
    # i = 0
    # while i < len(eta):
    #     n_vec[i]=np.array([])
    #     i+=1

    with open(n100_file, 'r') as n100_file:
        value_for_eta = ''
        prev = ''
        i = 0
        while i < len(eta):
            j = 0
            while True:
                line = n100_file.readline()  # Jump n = eta in file
                line = n100_file.readline()
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
    numbers = [i for i in range(1501)]

    while i < len(eta):
        if(i==0 or i==10 or i==20 or i==30 or i==40 or i==50):
            label = "η=" + str(eta[i])
            plt.plot(numbers, n_vec[i], label=label)
        i += 1

    # # Create boxes of info for N values
    # for n in eta:
    #     plt.text(5.1, 0.25 * n, f"η={n}", verticalalignment="center")

    # Customize the plot
    plt.ylabel("Va")
    plt.xlabel("t")
    plt.title("Time and Va variation according to η with density " + str(density))
    plt.ylim(0, 1)
    plt.xlim(0, 1501)
    plt.legend()

    # Show the plot
    plt.show()


if __name__ == "__main__":
    main()
