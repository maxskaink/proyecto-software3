import { SubjectOutcome } from "./SubjectOutcomeDTO";

export interface SubjectCompetency {
    id: number;
    description: string;
    programCompetencyId: number;
    subjectOutcomes?: SubjectOutcome[];
  }