# Entity

A JPA entity is a Java class mapped to a database table.
Each instance = one row. Each field = one column.

## Key annotations
- `@Entity` — marks the class as a DB table
- `@Table(name = "users")` — explicit table name (avoid reserved words)
- `@Column` — customizes column behavior (nullable, unique...)
- `@Enumerated` — stores enum as string in DB
- `@PrePersist` — runs before INSERT (used for timestamps)

## PanacheEntity
Quarkus helper that gives you:
- `id` field for free
- built-in methods: `findById()`, `listAll()`, `persist()`, `delete()`
- no need to write repository boilerplate for basic operations