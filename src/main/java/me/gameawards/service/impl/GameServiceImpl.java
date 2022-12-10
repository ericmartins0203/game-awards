package me.gameawards.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import me.gameawards.domain.model.Game;
import me.gameawards.domain.model.GameRepository;
import me.gameawards.service.GameService;
import me.gameawards.service.exception.BusinessException;
import me.gameawards.service.exception.NoContentException;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository repository;

    @Override
    public List<Game> findAll() {
        return repository.findAll(Sort.by(Direction.DESC, "votes"));
    }
    
    @Override
    public Game findById(Long id) {
        Optional<Game> game = repository.findById(id);
        return game.orElseThrow(() -> new NoContentException()); 
    }

    @Override
    public void insert(Game game) {
        if (Objects.nonNull(game.getId())) {
            throw new BusinessException("O ID precisa ser diferente de NULL na incluão");
        }
        repository.save(game);
    }

    @Override
    public void update(Long id, Game game) {
        Game gameDb = findById(id);
        if (gameDb.getId().equals(game.getId())) {
            repository.save(game);
        } else {
            throw new BusinessException("Os IDs para alteração são divergentes.");
        }
    }

    @Override
    public void delete(Long id) {
        Game gameDb = findById(id);
        repository.delete(gameDb);
    }

    @Override
    public void vote(Long id) {
        Game gameDb = findById(id);
       gameDb.setVotes(gameDb.getVotes() + 1);
       update(id, gameDb);
    }
}
