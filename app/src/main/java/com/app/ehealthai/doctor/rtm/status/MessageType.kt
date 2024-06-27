package com.app.ehealthai.doctor.rtm.status

enum class MessageType {

    TEXT("TEXT"),
    RAW("RAW"),
    OPENING_MSGS("OPENING_MSGS"),
    STATUS_START_TYPING_MSG("START_TYPING_MSG"),
    STOP_STATUS("STOP_STATUS"),
    ONLINE("ONLINE"),
    MUTE_MIC("MUTE_MIC"),
    MIC_ON("MIC_ON"),
    TURN_ON_CAMERA("TURN_ON_CAMERA"),
    TURN_OFF_CAMERA("TURN_OFF_CAMERA"),
    OPENING_NOTES_SECTION("OPENING_NOTES_SECTION"),
    SAVING_NOTES("SAVING_NOTE"),
    VIEWING_PATIENT_DETAILS("VIEWING_PATIENT_DETAILS"),
    VIEWING_PATIENT_REPORTS("VIEWING_PATIENT_REPORTS"),
    START_WRITING_NOTES("START_WRITING_NOTES"),
    VIEW_NOTES("VIEW_NOTES"),
    WATCHING_VITAL_HISTORY("WATCHING_VITAL_HISTORY"),
    ADDING_MEDICINE("ADDING_MEDICINE"),
    WATCHING_MEDICAL_HISTORY("WATCHING_MEDICAL_HISTORY"),
    FILE("FILE"),
    IMAGE("IMAGE");

    private var type: String
    constructor(msgType: String) {
        type = msgType
    }

    companion object {
        fun fromValue(msgType: String?): MessageType? {
            return when (msgType) {
                "RAW" -> RAW
                "START_TYPING_MSG" -> STATUS_START_TYPING_MSG
                "STOP_STATUS" -> STOP_STATUS
                "ONLINE" -> ONLINE
                "FILE" -> FILE
                "IMAGE" -> IMAGE
                "ADDING_MEDICINE" -> ADDING_MEDICINE
                "OPENING_NOTES_SECTION" -> OPENING_NOTES_SECTION
                "START_WRITING_NOTES" -> START_WRITING_NOTES
                "WATCHING_VITAL_HISTORY" -> WATCHING_VITAL_HISTORY
                "WATCHING_MEDICAL_HISTORY" -> WATCHING_MEDICAL_HISTORY
                "VIEWING_PATIENT_DETAILS" -> VIEWING_PATIENT_DETAILS
                "SAVING_NOTE" -> SAVING_NOTES
                "OPENING_MSGS" -> OPENING_MSGS
                "MUTE_MIC" -> MUTE_MIC
                "MIC_ON" -> MIC_ON
                "VIEW_NOTES" -> VIEW_NOTES
                "TURN_ON_CAMERA" -> TURN_ON_CAMERA
                "TURN_OFF_CAMERA" -> TURN_OFF_CAMERA
                else -> TEXT
            }
        }
    }
    override fun toString(): String {
        return type
    }
}