import { SubjectCompetency } from "./SubjectDTO";

// ProgramCompetency (para competencias de programa)
export interface ProgramCompetency {
  id: number;
  description: string;
  level: string;
  programOutcome: ProgramOutcome;
  subjectCompetency: SubjectCompetency[];
}

// ProgramOutcome
export interface ProgramOutcome {
  id: number;
  description: string;
}
