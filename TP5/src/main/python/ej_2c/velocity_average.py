def main():
    with open('./output_pedestrian.txt', 'r') as archivo:
        lineas = archivo.readlines()
    velocidades = 0
    amount = 0

    # Iterar sobre las líneas y extraer las velocidades
    for linea in lineas:
        if not linea.strip():  # Skip empty lines
            continue
        palabras = linea.split()
        velocidad = float(palabras[1])
        velocidades += velocidad
        amount += 1

    print(velocidades/amount)


if __name__ == '__main__':
    main()
