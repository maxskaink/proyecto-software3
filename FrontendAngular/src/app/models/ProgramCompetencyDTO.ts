import { ProgramOutcome } from "./ProgramOutcomeDTO";
import { SubjectCompetency } from "./SubjectCompetencyDTO";

export interface ProgramCompetency {
    id: number;
    description: string;
    level: string;
    programOutcome: ProgramOutcome;
    subjectCompetency: SubjectCompetency[];
  }