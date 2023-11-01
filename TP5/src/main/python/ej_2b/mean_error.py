def main():
    mean_err = []
    tau = ['0,1', '0,2', '0,3', '0,4', '0,5', '0,6', '0,7', '0,8', '0,9', '1', '1,1', '1,2', '1,3', '1,4', '1,5', '1,6', '1,7', '1,8', '1,9', '2', '2,1', '2,2', '2,3', '2,4', '2,5', '2,6', '2,7', '2,8', '2,9']
    id = '2'
    type = 'deacceleration'
    for t in tau:
        # Leer datos del primer archivo
        with open('../output_2.txt', 'r') as archivo1:
            lineas1 = archivo1.readlines()

        # Leer datos del segundo archivo
        with open('../output_' + id + '_' + t + '_' + type + '.txt', 'r') as archivo2:
            lineas2 = archivo2.readlines()

        # Crear listas para almacenar los valores de velocidad
        velocidades1 = []
        velocidades2 = []

        # Iterar sobre las líneas y extraer las velocidades
        for linea in lineas1:
            if not linea.strip():  # Skip empty lines
                continue
            palabras = linea.split()
            tiempo1 = float(palabras[0])
            velocidad1 = float(palabras[1])
            velocidades1.append((tiempo1, velocidad1))

        for linea in lineas2:
            if not linea.strip():  # Skip empty lines
                continue
            palabras = linea.split()
            tiempo2 = float(palabras[0])
            velocidad2 = float(palabras[1])
            velocidades2.append((tiempo2, velocidad2))

        # Calcular el error medio entre las velocidades
        error_total = 0.0

        for v1, v2 in zip(velocidades1, velocidades2):
            tiempo1, velocidad1 = v1
            tiempo2, velocidad2 = v2
            error = abs(velocidad1 - velocidad2)
            error_total += error

        error_medio = error_total / len(velocidades1)

        mean_err.append(error_medio)
    print(f"Error medio entre las velocidades: {mean_err}")

if __name__ == '__main__':
    main()
