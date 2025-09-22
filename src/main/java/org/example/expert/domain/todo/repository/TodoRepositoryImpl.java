package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.UnifiedTodoResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo content = jpaQueryFactory
                .selectFrom(todo)
                .join(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(content);
    }

    @Override
    public Page<UnifiedTodoResponse> findByUnified(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String nickname, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;
        QManager manager = QManager.manager;
        QComment comment = QComment.comment;

        BooleanBuilder builder = new BooleanBuilder();

        if (title != null) builder.and(todo.title.contains(title));
        if (startDateTime != null) builder.and(todo.createdAt.after(startDateTime));
        if (endDateTime != null) builder.and(todo.createdAt.before(endDateTime));
        if (nickname != null) builder.and(todo.user.nickname.contains(nickname));

        List<UnifiedTodoResponse> content = jpaQueryFactory
                .select(Projections.constructor(UnifiedTodoResponse.class, todo.title, manager.id.countDistinct(), comment.id.countDistinct()))
                .from(todo)
                .leftJoin(todo.user, user)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(builder)
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(jpaQueryFactory
                    .select(todo.id.countDistinct())
                    .from(todo)
                    .where(builder)
                    .fetchOne())
                .orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}
