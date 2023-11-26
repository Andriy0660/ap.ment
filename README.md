
# Ap.ment-demo using n-tier architecture pattern

## In Progress...

### In this project, I practiced JDBC Template, JUnit5&Mockito, Spring Security very hard, developed 4 different security configurations:
- using UserDetails and standart Spring Security internal flow
- using own provider(`without` UserDetails)
- using `only` JWT(removed own provider)
- using `only` Spring Security Context(http session `with` state, `without` JWT)

#### Also in this project I practiced OOP and used my knowledge of internal Spring Security code(for larger details - I used the pattern used in Spring Security Provider Manager that chooses the needed provider) by writing mappers:

public interface `IMapper` {

    <V, T> T map(V source);
    <V> boolean supports(V source);
}

public class MapperManager {

    private final List<IMapper> mappers;
    public <V,T> T map(V source){
        for(IMapper mapper : mappers){
            if(mapper.supports(source))return mapper.map(source);
        }
        throw new BadRequestException("Unsupported type");
    }
}

public class `UserMapper` implements IMapper{

    public UserDTO mapUser(User user){
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
    public Set<UserDTO> mapUser(Set<User> users){
        return users.stream().map(this::mapUser)
                .collect(Collectors.toSet());
    }

    public <V, T> T map(V source) {
        if(source instanceof User) return (T)mapUser((User)source);
        else return (T)mapUser((Set<User>) source);
    }
    public <V> boolean supports(V source){
        return (source instanceof User) ||
                (source instanceof Set && ((Set<?>)source).iterator().next() instanceof User);
    }
}

public class `FriendRequestMapper` implements IMapper{

    private final UserMapper userMapper;
    public UserDTO mapRequest(FriendRequest friendRequest){
        return userMapper.map(friendRequest.getSender());
    }
    public Set<UserDTO> mapRequest(Set<FriendRequest> requests){
        return requests.stream().map(this::mapRequest)
                .collect(Collectors.toSet());
    }

    public <V, T> T map(V source) {
        if(source instanceof FriendRequest) return (T)mapRequest((FriendRequest) source);
        else return (T)mapRequest((Set<FriendRequest>) source);
    }
    public <V> boolean supports(V source){
        return (source instanceof FriendRequest) ||
                (source instanceof Set && ((Set<?>)source).iterator().next() instanceof FriendRequest);
    }

}



