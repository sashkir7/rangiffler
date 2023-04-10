package model;

import lombok.Builder;
import lombok.Data;
import sashkir7.grpc.Photo;
import sashkir7.grpc.User;

import java.util.*;

@Data
@Builder
public final class UserModel {
    private String username, password,
            firstname, lastname,
            avatar;

    @Builder.Default
    private Set<Photo> photos = new HashSet<>();

    @Builder.Default
    private Map<PartnerStatus, Set<UserModel>> partners = new HashMap<>();

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    public void addPartner(PartnerStatus status, UserModel partner) {
        partners.computeIfAbsent(status, k -> new HashSet<>());
        partners.get(status).add(partner);
    }

    public User toGrpc() {
        User.Builder builder = User.newBuilder()
                .setUsername(username)
                .setFirstname(firstname)
                .setLastname(lastname);
        // ToDo нужен ли id?!
//        if (id != null) {
//            builder.setId(id.toString());
//        }
        if (avatar != null) {
            builder.setAvatar(avatar);
        }
        return builder.build();
    }

}
