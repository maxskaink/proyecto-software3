import { SubjectOutcome } from "./SubjectOutcomeDTO";

export interface EvaluatorAssignment {
    id: number;
    termId: number;
    subjectOutcome: SubjectOutcome;
    evaluatorUid: string;
}
