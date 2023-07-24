type RegistrationStageMap = {
    [key: string]: string;
};

type RegistrationStage = {
    stageId: number;
    name: string;
    schoolId: number;
    startTime: string;
    endTime: string;
    remark: string;
    definer: string;
};

type RegistrationData = {
    stageMap: RegistrationStageMap;
    schoolName: string;
    stages: RegistrationStage[];
};
