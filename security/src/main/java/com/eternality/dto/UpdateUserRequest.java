package com.eternality.dto;

import java.util.Optional;

/**
 * Есть 3 состояния:
 * 1. Поле ненуловое, то есть Optional<T> != null.
 *      1.1. Значение в поле null -> обновляем на null в бд
 *      1.2. Значение в поле не null -> обновляем на не null в бд
 * 2. Поле пустое, то есть Optional<T> = Optional.empty().
 * Значение в поле null -> обновляем на null в бд
 * 3. Поле нуловое, то есть Optional<T> = null.
 * Значит ничего обновлять по этому полю не нужно.
 * --------------------------------------------------
 * Чтобы алгоритм выше можно было как бы поддержать в Kotlin, пришлось сделать двойной костыль:
 * 1. Dto сделать на Java, чтобы Kotlin смотрел на поля объекта как Type!
 * 2. Сделать delombok, иначе Kotlin, находясь с dto в одном модуле, не видит Lombok
 * Так что вот так :)
 */
public class UpdateUserRequest {
    private Optional<String> userName = null;
    private Optional<String> firstName = null;
    private Optional<String> lastName = null;
    private Optional<String> email = null;
    private Optional<String> phone = null;

    public UpdateUserRequest(Optional<String> userName,
                             Optional<String> firstName,
                             Optional<String> lastName,
                             Optional<String> email,
                             Optional<String> phone) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public UpdateUserRequest() {
    }

    public Optional<String> getUserName() {
        return userName;
    }

    public void setUserName(Optional<String> userName) {
        this.userName = userName;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(Optional<String> firstName) {
        this.firstName = firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public void setLastName(Optional<String> lastName) {
        this.lastName = lastName;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getPhone() {
        return phone;
    }

    public void setPhone(Optional<String> phone) {
        this.phone = phone;
    }

    public static UpdateUserRequestBuilder builder() {
        return new UpdateUserRequestBuilder();
    }

    public static class UpdateUserRequestBuilder {
        private Optional<String> userName;
        private Optional<String> firstName;
        private Optional<String> lastName;
        private Optional<String> email;
        private Optional<String> phone;

        UpdateUserRequestBuilder() {
        }

        public UpdateUserRequestBuilder userName(Optional<String> userName) {
            this.userName = userName;
            return this;
        }

        public UpdateUserRequestBuilder firstName(Optional<String> firstName) {
            this.firstName = firstName;
            return this;
        }

        public UpdateUserRequestBuilder lastName(Optional<String> lastName) {
            this.lastName = lastName;
            return this;
        }

        public UpdateUserRequestBuilder email(Optional<String> email) {
            this.email = email;
            return this;
        }

        public UpdateUserRequestBuilder phone(Optional<String> phone) {
            this.phone = phone;
            return this;
        }

        public UpdateUserRequest build() {
            return new UpdateUserRequest(userName, firstName, lastName, email, phone);
        }

        public String toString() {
            return "UpdateUserRequest.UpdateUserRequestBuilder(" +
                    "userName=" + this.userName +
                    ", firstName=" + this.firstName +
                    ", lastName=" + this.lastName +
                    ", email=" + this.email +
                    ", phone=" + this.phone +
                    ")";
        }
    }
}
