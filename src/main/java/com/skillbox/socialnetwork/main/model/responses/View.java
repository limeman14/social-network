package com.skillbox.socialnetwork.main.model.responses;

public class View{
    public interface GeneralView {}
    public interface LoginResponse extends MyInfoResponse{}
    public interface MyInfoResponse extends GeneralView{}
}
