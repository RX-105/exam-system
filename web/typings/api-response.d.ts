// response entity
type R = {
    status: number;
    message: string;
    data: { [key: string]: any };
}
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

// /api/student/user/this
type School = {
    schoolId: number;
    code: string;
    name: string;
    address: string;
    zipcode: number;
    contactNumber: string;
    examName: string;
    recruitYears: string;
}

type Major = {
    id: number;
    name: string;
    schoolId: number;
    applicantCount: number;
    enrollmentCount: number;
    acceptScore: number;
    admissionCount: number;
}

type UserInfo = {
    userId: number;
    tableId: null;
    name: string;
    realname: string;
    identityId: string;
    gender: string;
    politicalStatus: string;
    phone: number;
    source: string;
    graduateSchool: string;
    graduateTime: string;
    isCurrent: boolean;
    isScience: boolean;
    school: School;
    major: Major;
    englishLevel: string;
    homeAddress: string;
    nationality: string;
    birthday: string;
    avatarName: string;
    roomId: number;
    seatId: number;
    admissionId: null;
    isConfirmed: boolean;
    receiver: string;
    contactAddress: string;
    zipcode: number;
    contactNumber: string;
};
