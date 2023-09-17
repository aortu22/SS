import matplotlib.pyplot as plt

def get_perimeter_length():
    with open("../java/main/static.txt", 'r') as config_file:
        L = float(next(config_file))
        L_fixed = float(next(config_file))
        total_perimeter = L_fixed * 3 + (L_fixed - L) + L_fixed *2 + L
        return total_perimeter

def main():

    presiones_por_segundo = {}

    with open('impulse.txt', 'r') as archivo:
        for linea in archivo:
            tiempo, presion = map(float, linea.strip().split())
            segundo = int(tiempo)

            # Verificar si ya existe una suma para ese segundo en el diccionario
            if segundo in presiones_por_segundo:
                presiones_por_segundo[segundo] += presion
            else:
                presiones_por_segundo[segundo] = presion

    tiempos = list(presiones_por_segundo.keys())
    presiones = list(presiones_por_segundo.values())

    #longitud total del perimetro
    perimeter_length = get_perimeter_length()

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