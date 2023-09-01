import matplotlib.pyplot as plt
import numpy as np

def main():
    density = 0.16
    n40_file = './order40.txt'
    n100_file = './order100.txt'
    n400_file = './order400.txt'
    n4000_file = './order4000.txt'

    eta = [0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0]

    N = [40, 100, 400, 4000]

    n40_vec = np.zeros((len(eta), 3501), dtype=float)
    n40_mean_vec = np.zeros(len(eta), dtype=float)
    n40_std_vec = np.zeros(len(eta), dtype=float)
    with open(n40_file, 'r') as n40_file:
        prev = ''
        i = 0
        while i < len(eta):
            j = 0
            while j < 500:
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


    n100_vec = np.zeros((len(eta), 3601), dtype=float)
    n100_mean_vec = np.zeros(len(eta), dtype=float)
    n100_std_vec = np.zeros(len(eta), dtype=float)
    with open(n100_file, 'r') as n100_file:
        prev = ''
        i = 0
        while i < len(eta):
            j = 0
            while j < 400:
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

    n400_vec = np.zeros((len(eta), 3201), dtype=float)
    n400_mean_vec = np.zeros(len(eta), dtype=float)
    n400_std_vec = np.zeros(len(eta), dtype=float)
    with open(n400_file, 'r') as n400_file:
        prev = ''
        i = 0
        while i < len(eta):
            j = 0
            while j < 800:
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

    n4000_vec = np.zeros((len(eta), 3001), dtype=float)
    n4000_mean_vec = np.zeros(len(eta), dtype=float)
    n4000_std_vec = np.zeros(len(eta), dtype=float)
    with open(n4000_file, 'r') as n4000_file:
        prev = ''
        i = 0
        while i < len(eta):
            j = 0
            while j < 1000:
                n4000_file.readline()
                j += 1
            j = 0
            while True:
                line = n4000_file.readline()
                if not line:  # If readline() returns an empty string, we've reached the end of the file
                    break
                if line == "\n":
                    if prev != "\n":
                        break
                else:
                    n4000_vec[i, j] = float(line[:-1].replace(",", "."))
                j += 1
                prev = line

            n4000_mean_vec[i] = np.mean(n4000_vec[i])
            n4000_std_vec[i] = np.std(n4000_vec[i])
            i += 1

    plt.figure(figsize=(10, 6))



    # Create lines for the vectors
    plt.errorbar(eta, n40_mean_vec, yerr=n40_std_vec, fmt='-o', label="N=40")
    plt.errorbar(eta, n100_mean_vec, yerr=n100_std_vec, fmt='-o', label="N=100")
    plt.errorbar(eta, n400_mean_vec, yerr=n400_std_vec, fmt='-o', label="N=400")
    plt.errorbar(eta, n4000_mean_vec, yerr=n400_std_vec, fmt='-o', label="N=4000")


    # Create boxes of info for N values
    for n in N:
        plt.text(5.1, 0.25 * n, f"N={n}", verticalalignment="center")

    # Customize the plot
    plt.xlabel("noise(η)")
    plt.ylabel("Va")
    # plt.title("Va variation according to η with density " + str(density))
    plt.xlim(0, 5.0)
    plt.ylim(0, 1)
    plt.legend()

    # Show the plot
    plt.show()
if __name__ == "__main__":
    main()