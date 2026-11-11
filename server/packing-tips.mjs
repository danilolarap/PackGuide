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
  ['Reparte el peso entre ambos lados y deja documentos y objetos de uso frecuente en un bolsillo accesible.', 'Distribute weight between both sides and keep documents and frequently used items in an accessible pocket.']
];

export function createPackingTips() {
  const assigned = new Map();
  let next = 0;
  return (photoBase64, language) => {
    const id = createHash('sha256').update(Buffer.from(photoBase64, 'base64')).digest('hex');
    if (!assigned.has(id)) {
      if (assigned.size >= 1024) assigned.delete(assigned.keys().next().value);
      assigned.set(id, next++ % tips.length);
    }
    const index = assigned.get(id);
    const prefix = language === 'en' ? 'PackGuide tip (prewritten)' : 'Consejo de PackGuide (pregrabado)';
    return `${prefix} ${index + 1}/10: ${tips[index][language === 'en' ? 1 : 0]}`;
  };
}
