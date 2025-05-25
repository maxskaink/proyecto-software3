import { ProgramOutcome } from "./ProgramOutcome";
import { SubjectCompetency } from "./SubjectCompetency";

export interface ProgramCompetency {
    id: number;
    description: string;
    level: string;
    programOutcome: ProgramOutcome;
    subjectCompetency: SubjectCompetency[];
  }