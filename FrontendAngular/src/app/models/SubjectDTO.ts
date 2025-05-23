
export interface SubjectDTO {
    id: number;
    title: string;
    description: string;
}

export interface SubjectCompetency {
  id: number;
  description: string;
  level: string;
  programCompetencyId: number;
}

export interface SubjectOutcome {
  id: number;
  description: string;
  rubric: any | null; // Puedes reemplazar 'any' por Rubric si lo necesitas
}