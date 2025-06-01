import { SubjectOutcome } from "./SubjectOutcomeDTO";

export interface SubjectCompetency {
    id: number;
    description: string;
    programCompetencyId: number;
  }

  export interface SubjectCompetencyPostDTO {
    competency: SubjectCompetency;
    outcomes: SubjectOutcome[];
  }
