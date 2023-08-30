import matplotlib.pyplot as plt
import numpy as np

def main():
    n40_file = './order_density40.txt'
    n100_file = './order_density100.txt'
    n400_file = './order_density400.txt'

    density = [0.25, 1.25, 1.5, 1.75, 2.0]

    N = [40, 100, 400]

    n40_vec = np.zeros((len(density), 1301), dtype=float)
    n40_mean_vec = np.zeros(len(density), dtype=float)
    n40_std_vec = np.zeros(len(density), dtype=float)
    with open(n40_file, 'r') as n40_file:
        prev = ''
        i = 0
        while i < len(density):
            j = 0
            while j < 2700:
                n40_file.readline()
                j += 1
            j = 0
            while True:
                line = n40_file.readline()
                if not line:  # If readline() returns an empty string, we've reached the end of the file
                    break
                if line == "\n":
                    if prev != "\n":
                        break
                else:
                    n40_vec[i, j] = float(line[:-1].replace(",", "."))
                j += 1
                prev = line

            n40_mean_vec[i] = np.mean(n40_vec[i])
            n40_std_vec[i] = np.std(n40_vec[i])
            i += 1


    n100_vec = np.zeros((len(density), 1501), dtype=float)
    n100_mean_vec = np.zeros(len(density), dtype=float)
    n100_std_vec = np.zeros(len(density), dtype=float)
    with open(n100_file, 'r') as n100_file:
        prev = ''
        i = 0
        while i < len(density):
            j = 0
            while j < 2500:
                n100_file.readline()
                j += 1
            j = 0
            while True:
                line = n100_file.readline()
                if not line:  # If readline() returns an empty string, we've reached the end of the file
                    break
                if line == "\n":
                    if prev != "\n":
                        break
                else:
                    n100_vec[i, j] = float(line[:-1].replace(",", "."))
                j += 1
                prev = line

            n100_mean_vec[i] = np.mean(n100_vec[i])
            n100_std_vec[i] = np.std(n100_vec[i])
            i += 1

    n400_vec = np.zeros((len(density), 2001), dtype=float)
    n400_mean_vec = np.zeros(len(density), dtype=float)
    n400_std_vec = np.zeros(len(density), dtype=float)
    with open(n400_file, 'r') as n400_file:
        prev = ''
        i = 0
        while i < len(density):
            j = 0
            while j < 2000:
                n400_file.readline()
                j += 1
            j = 0
            while True:
                line = n400_file.readline()
                if not line:  # If readline() returns an empty string, we've reached the end of the file
                    break
                if line == "\n":
                    if prev != "\n":
                        break
                else:
                    n400_vec[i, j] = float(line[:-1].replace(",", "."))
                j += 1
                prev = line

            n400_mean_vec[i] = np.mean(n400_vec[i])
            n400_std_vec[i] = np.std(n400_vec[i])
            i += 1


    plt.figure(figsize=(10, 6))



    # Create lines for the vectors
    plt.errorbar(density, n40_mean_vec, yerr=n400_std_vec / np.sqrt(len(n40_std_vec)), fmt='-o',label="N=40")
    plt.errorbar(density, n100_mean_vec, yerr=n100_std_vec / np.sqrt(len(n100_std_vec)), fmt='-o',label="N=100")
    plt.errorbar(density, n400_mean_vec, yerr=n400_std_vec / np.sqrt(len(n400_std_vec)), fmt='-o',label="N=400")


    # Create boxes of info for N values
    for n in N:
        plt.text(5.1, 0.25 * n, f"N={n}", verticalalignment="center")

    # Customize the plot
    plt.xlabel("density(N/L^2)")
    plt.ylabel("Va")
    # plt.title("Va variation according to η with density " + str(density))
    plt.xlim(0.25, 2.0)
    plt.ylim(0, 1)
    plt.legend()

    # Show the plot
    plt.show()
if __name__ == "__main__":
    main()