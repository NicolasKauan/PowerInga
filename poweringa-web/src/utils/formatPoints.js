const pointsFormatter = new Intl.NumberFormat("pt-BR");

export const formatPoints = (points) => pointsFormatter.format(points);
