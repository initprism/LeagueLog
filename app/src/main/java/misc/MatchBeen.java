package misc;

import net.rithms.riot.api.endpoints.match.dto.Participant;
import net.rithms.riot.api.endpoints.match.dto.ParticipantIdentity;
import net.rithms.riot.constant.Platform;

public class MatchBeen {

    Participant participant;
    ParticipantIdentity participantIdentity;
    Platform platform;
    long duration;
    long topDamage;

    public MatchBeen(Participant participant, ParticipantIdentity participantIdentity, Platform platform, long duration, long topDamage){
        this.participant = participant;
        this.participantIdentity = participantIdentity;
        this.platform = platform;
        this.duration = duration;
        this.topDamage = topDamage;
    }

    public Participant getParticipant() {
        return participant;
    }

    public ParticipantIdentity getParticipantIdentity() {
        return participantIdentity;
    }

    public Platform getPlatform() {
        return platform;
    }

    public long getDuration(){ return duration; }

    public long getTopDamage() {
        return topDamage;
    }
}
