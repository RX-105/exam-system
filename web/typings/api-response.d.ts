
// /api/student/registration-info
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

//
type PageInfo = {
    totalPages: number;
    currentElements: number;
    totalElements: number;
    thisPage: number;
};
type LogContent = {
    logId: number;
    username: string;
    groupName: string | null;
    time: string;
    ip: string;
    action: string;
    extras: string;
};
type LogData = {
    pageInfo: PageInfo;
    content: LogContent[];
};
