import { createHash } from 'node:crypto';

// Editorial suggestions, never presented as observations made by the vision model.
const tips = [
  ['Enrolla las camisetas ligeras y agrúpalas por uso para sacar una sin desordenar el resto.', 'Roll light T-shirts and group them by use so you can remove one without disturbing the rest.'],
  ['Dobla los pantalones siguiendo sus costuras y distribúyelos en capas planas para evitar bultos.', 'Fold trousers along their seams and lay them in flat layers to avoid bulky spots.'],
  ['Coloca el calzado en bolsas separadas, cerca de las ruedas, para aislar la ropa de la suciedad.', 'Place shoes in separate bags near the wheels to keep dirt away from clothes.'],
  ['Agrupa calcetines y ropa interior en una bolsa pequeña para evitar que se dispersen.', 'Group socks and underwear in a small pouch to keep them from scattering.'],
  ['Deja arriba la ropa de la primera jornada para acceder a ella sin vaciar la maleta.', 'Keep your first day’s clothes on top so you can reach them without unpacking everything.'],
  ['Guarda los artículos de aseo en un neceser cerrado y fácil de extraer para contener posibles fugas.', 'Keep toiletries in a closed, easy-to-remove pouch to contain possible leaks.'],
  ['Separa cargadores y cables en un estuche y recógelos individualmente para evitar enredos.', 'Separate chargers and cables in a case and coil each cable to prevent tangles.'],
  ['Usa organizadores de tela por conjuntos; comprime solo prendas que toleren las arrugas.', 'Use fabric packing cubes for outfits; compress only clothes that tolerate wrinkles.'],
  ['Reserva una bolsa para la ropa usada y así mantenla separada de las prendas limpias.', 'Reserve a bag for worn clothes to keep them separate from clean garments.'],
  ['Reparte el peso entre ambos lados y deja documentos y objetos de uso frecuente en un bolsillo accesible.', 'Distribute weight between both sides and keep documents and frequently used items in an accessible pocket.'],
  ['Rellena los huecos pequeños con calcetines sin forzar los cierres.', 'Fill small gaps with socks without straining zippers.'],
  ['Lleva las prendas delicadas dentro de una funda para reducir roces y enganches.', 'Place delicate clothes in a garment bag to reduce rubbing and snags.'],
  ['Coloca los objetos pesados en la base y evita apoyarlos sobre prendas delicadas.', 'Place heavy objects at the bottom and keep them off delicate clothes.'],
  ['Prepara conjuntos completos antes de empacar para reducir prendas que no vas a utilizar.', 'Plan complete outfits before packing to reduce clothes you will not use.'],
  ['Guarda cinturones alrededor del perímetro interior para aprovechar los bordes.', 'Place belts around the inside perimeter to make use of the edges.'],
  ['Deja una bolsa plegable vacía para artículos que necesites separar durante el viaje.', 'Pack an empty foldable bag for items you need to separate during the trip.'],
  ['Protege los objetos frágiles con prendas suaves y evita que se muevan dentro de la maleta.', 'Cushion fragile items with soft clothes and prevent them from moving inside the suitcase.'],
  ['Abrocha las correas interiores con suavidad para sujetar el contenido sin aplastarlo.', 'Fasten the interior straps gently to secure the contents without crushing them.'],
  ['Comprueba el acceso a documentos, medicinas y artículos de la primera noche antes de cerrar.', 'Check access to documents, medicines and first-night items before closing.'],
  ['Reserva algo de espacio para volver a empacar durante el viaje sin presionar la tapa.', 'Leave some space to repack during the trip without pressing down on the lid.']
];

export function createPackingTips() {
  const assigned = new Map();
  return (photoBase64, language) => {
    const id = createHash('sha256').update(Buffer.from(photoBase64, 'base64')).digest('hex');
    if (!assigned.has(id)) {
      if (assigned.size >= 1024) assigned.delete(assigned.keys().next().value);
      const indices = Array.from({ length: tips.length }, (_, i) => i);
      for (let i = 0; i < 2; i++) {
        const j = i + Math.floor(Math.random() * (indices.length - i));
        [indices[i], indices[j]] = [indices[j], indices[i]];
      }
      assigned.set(id, indices.slice(0, 2));
    }
    const heading = language === 'en' ? 'PACKING TIPS' : 'CONSEJOS PARA ORGANIZAR TU MALETA';
    return heading + '\n' + assigned.get(id).map((index, number) => `${number + 1}. ${tips[index][language === 'en' ? 1 : 0]}`).join('\n');
  };
}
