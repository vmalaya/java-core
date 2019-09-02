package sigma.software.messagerepository.domain.event;

import sigma.software.messagerepository.domain.event.api.DomainEvent;

import java.util.UUID;

public class FriendRequestSentEvent implements DomainEvent {
    private final UUID aggregateId;
    private final UUID friendId;

    public FriendRequestSentEvent(UUID aggregateId, UUID friendId) {
        this.aggregateId = aggregateId;
        this.friendId = friendId;
    }

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    public UUID getFriendId() {
        return friendId;
    }
}
