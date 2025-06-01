import { RubricDTO } from "./RubricDTO";

export interface SubjectOutcome {
    id: number;
    description: string;
    rubric: RubricDTO | null;
    idCompetencyAssignment: number;
  }
